package se.miniwa.diamond.client;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class DiamondClient {
    private Gson gson = new Gson();
    private MediaType jsonType = MediaType.get("application/json");
    private HttpUrl baseUrl = HttpUrl.parse("http://diamonds.etimo.se/api");
    private HttpUrl getBoardsUrl = baseUrl.newBuilder()
            .addPathSegment("Boards")
            .build();
    private HttpUrl registerBotUrl = baseUrl.newBuilder()
            .addPathSegment("Bots")
            .build();
    private OkHttpClient client;

    public DiamondClient(OkHttpClient client) {
        this.client = client;
    }

    public BoardDto[] getBoards() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(getBoardsUrl)
                .build();

        try(Response resp = client.newCall(request).execute()) {
            if(resp.code() == 200) {
                String json = resp.body().string();
                return gson.fromJson(json, BoardDto[].class);
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
                    throw new BoardFullException("Board is full or bot is already on the board.");
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

        Request request = new Request.Builder()
                .url(registerBotUrl)
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

    public void moveBot(String id, String token, String direction) throws IOException, InvalidBotException,
            InvalidBoardException {
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
            if(code != 200) {
                if(code == 403) {
                    throw new InvalidBotException(
                            "Bot does not exist, is not on the board or is trying to move too fast.");
                } else if (code == 404) {
                    throw new InvalidBoardException("Board does not exist.");
                } else {
                    throw new IOException("Invalid response code " + code + ".");
                }
            }
        }
    }

    public class JoinBoardDto {
        public String botToken;
    }

    public class RegisterBotDto {
        public String name;
        public String email;
    }

    public class RegisterBotResponseDto {
        public String id;
        public String name;
        public String email;
        public String token;
    }

    public class MoveBotDto {
        public String botToken;
        public String direction;
    }

    public class PositionDto {
        public int x;
        public int y;
    }

    public class DiamondDto {
        public int points;
        public int x;
        public int y;
    }

    public class GameObjectDto {
        public String name;
        public boolean isBlocking;
        public PositionDto position;

        // Optional for teleporters
        public String linkedTeleporterString;
    }

    public class BotDto {
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

    public class BoardDto {
        public String id;
        public int width;
        public int height;
        public int minimumDelayBetweenMoves;
        public List<DiamondDto> diamonds;
        public List<GameObjectDto> gameObjects;
        public List<BotDto> bots;
    }
}
