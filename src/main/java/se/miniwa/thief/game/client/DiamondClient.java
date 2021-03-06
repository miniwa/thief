package se.miniwa.thief.game.client;

import com.google.gson.Gson;
import okhttp3.*;
import se.miniwa.thief.game.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiamondClient {
    private Gson gson = new Gson();
    private MediaType jsonType = MediaType.get("application/json");
    private OkHttpClient client;
    private HttpUrl baseUrl;

    public DiamondClient(OkHttpClient client) {
        this.client = client;
        this.baseUrl = HttpUrl.parse("http://diamonds.etimo.se/api");
    }

    public DiamondClient(OkHttpClient client, HttpUrl baseUrl) {
        this.client = client;
        this.baseUrl = baseUrl;
    }

    public List<Board> getBoards() throws IOException {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("Boards")
                .build();

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        try(Response resp = client.newCall(request).execute()) {
            if(resp.code() == 200) {
                String json = resp.body().string();
                BoardDto[] boards = gson.fromJson(json, BoardDto[].class);

                List<Board> result = new ArrayList<>();
                for(BoardDto board : boards) {
                    result.add(parseBoard(board));
                }
                return result;
            } else {
                throw new IOException("Response unsuccessful." + resp.code());
            }
        }
    }

    public void joinBoard(String id, String token) throws IOException, InvalidBotException, InvalidBoardException,
            BoardFullException {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("Boards")
                .addPathSegment(id)
                .addPathSegment("join")
                .build();

        JoinBoardDto joinDto = new JoinBoardDto();
        joinDto.botToken = token;

        RequestBody body = RequestBody.create(jsonType, gson.toJson(joinDto));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try(Response resp = client.newCall(request).execute()) {
            int code = resp.code();
            if(code != 200) {
                if(code == 403) {
                    throw new InvalidBotException("Bot does not exist.");
                } else if (code == 404) {
                    throw new InvalidBoardException("Board does not exist.");
                } else if (code == 409) {
                    throw new BoardFullException("Board is full or api is already on the board.");
                } else {
                    throw new IOException("Unexpected response code " + code + ".");
                }
            }
        }
    }

    public String registerBot(String name, String email) throws IOException {
        RegisterBotDto registerBot = new RegisterBotDto();
        registerBot.name = name;
        registerBot.email = email;
        RequestBody body = RequestBody.create(jsonType, gson.toJson(registerBot));

        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("Bots")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try(Response resp = client.newCall(request).execute()) {
            if(resp.code() == 200) {
                String json = resp.body().string();
                RegisterBotResponseDto responseDto = gson.fromJson(json, RegisterBotResponseDto.class);
                return responseDto.token;
            } else {
                throw new IOException("Invalid response code.");
            }
        }
    }

    public Board moveBot(String id, String token, String direction) throws IOException, InvalidBotException,
            InvalidBoardException, InvalidMoveException {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("Boards")
                .addPathSegment(id)
                .addPathSegment("move")
                .build();

        MoveBotDto moveDto = new MoveBotDto();
        moveDto.botToken = token;
        moveDto.direction = direction;
        RequestBody body = RequestBody.create(jsonType, gson.toJson(moveDto));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try(Response resp = client.newCall(request).execute()) {
            int code = resp.code();
            if(code == 200) {
                String json = resp.body().string();
                return parseBoard(gson.fromJson(json, BoardDto.class));
            } else {
                if(code == 403) {
                    throw new InvalidBotException(
                            "Bot does not exist, is not on the board or is trying to move too fast.");
                } else if (code == 404) {
                    throw new InvalidBoardException("Board does not exist.");
                } else if(code == 409) {
                    throw new InvalidMoveException("Move is blocked or not valid.");
                } else {
                    throw new IOException("Invalid response code " + code + ".");
                }
            }
        }
    }

    private Board parseBoard(BoardDto dto) {
        Portal portal = parsePortal(dto.gameObjects);
        List<Diamond> diamonds = new ArrayList<>();
        for(DiamondDto diamondDto : dto.diamonds) {
            diamonds.add(parseDiamond(diamondDto));
        }
        List<Player> players = new ArrayList<>();
        for(BotDto botDto : dto.bots) {
            players.add(parsePlayer(botDto));
        }

        return Board.builder()
                .setId(dto.id)
                .setWidth(dto.width)
                .setHeight(dto.height)
                .setMinimumDelayBetweenMoves(Duration.ofMillis(dto.minimumDelayBetweenMoves))
                .setPortal(portal)
                .setDiamonds(diamonds)
                .setPlayers(players)
                .build();
    }

    private Diamond parseDiamond(DiamondDto dto) {
        Position pos = Position.create(dto.x, dto.y);
        return Diamond.create(dto.points, pos);
    }

    private Portal parsePortal(List<GameObjectDto> dtos) {
        List<GameObjectDto> portalDtos = dtos.stream()
                .filter(dto -> dto.name.equals("Teleporter"))
                .collect(Collectors.toList());

        Position first =  parsePosition(portalDtos.get(0).position);
        Position second =  parsePosition(portalDtos.get(1).position);
        return Portal.create(first, second);
    }

    private Player parsePlayer(BotDto dto) {
        Position pos = Position.create(dto.position.x, dto.position.y);
        Position base = Position.create(dto.base.x, dto.base.y);
        Instant nextMoveDate = Instant.parse(dto.nextMoveAvailableAt);
        Instant roundOverDate = Instant.now().plusMillis(dto.millisecondsLeft);
        return Player.builder()
                .setName(dto.name)
                .setId(dto.botId)
                .setPosition(pos)
                .setBase(base)
                .setDiamondCount(dto.diamonds)
                .setScore(dto.score)
                .setNextMoveAvailableAt(nextMoveDate)
                .setRoundOverAt(roundOverDate)
                .build();
    }

    private Position parsePosition(PositionDto dto) {
        return Position.create(dto.x, dto.y);
    }

    private class JoinBoardDto {
        public String botToken;
    }

    private class RegisterBotDto {
        public String name;
        public String email;
    }

    private class RegisterBotResponseDto {
        public String id;
        public String name;
        public String email;
        public String token;
    }

    private class MoveBotDto {
        public String botToken;
        public String direction;
    }

    private class PositionDto {
        public int x;
        public int y;
    }

    private class DiamondDto {
        public int points;
        public int x;
        public int y;
    }

    private class GameObjectDto {
        public String name;
        public boolean isBlocking;
        public PositionDto position;

        // Optional for teleporters
        public String linkedTeleporterString;
    }

    private class BotDto {
        public String name;
        public String botId;
        public String timeJoined;
        public String nextMoveAvailableAt;
        public PositionDto base;
        public PositionDto position;
        public int diamonds;
        public int millisecondsLeft;
        public int score;
    }

    private class BoardDto {
        public String id;
        public int width;
        public int height;
        public int minimumDelayBetweenMoves;
        public List<DiamondDto> diamonds;
        public List<GameObjectDto> gameObjects;
        public List<BotDto> bots;
    }
}
