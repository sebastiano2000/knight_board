package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class KnightBoard {
    static class Point { public int x, y; }
    static class Board { public int width, height; public List<Point> obstacles; }
    static class CommandsWrapper { public List<String> commands; }
    static class Result { public int x, y; public String direction, status;
        public Result(int x, int y, String direction, String status) {
            this.x = x; this.y = y; this.direction = direction; this.status = status;
        }
        public Result(String status) {
            this.status = status;
        }
    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String boardApi = System.getenv("BOARD_API");
            String cmdsApi = System.getenv("COMMANDS_API");
            if (boardApi == null || cmdsApi == null) {
                throw new IllegalStateException("Environment variables BOARD_API and COMMANDS_API must be set");
            }

            Board board = fetchJson(boardApi, new TypeReference<Board>() {});
            CommandsWrapper wrapper = fetchJson(cmdsApi, new TypeReference<CommandsWrapper>() {});
            List<String> commands = wrapper.commands;

            // directions and moves
            String[] dirs = {"NORTH","EAST","SOUTH","WEST"};
            Map<String, Point> moves = Map.of(
                    "NORTH", createPoint(0,1),
                    "EAST",  createPoint(1,0),
                    "SOUTH", createPoint(0,-1),
                    "WEST",  createPoint(-1,0)
            );

            if (commands.isEmpty() || !commands.get(0).startsWith("START ")) {
                throw new IllegalArgumentException("First command must be START");
            }
            // initialize start
            String[] sp = commands.get(0).split(" ", 2)[1].split(",");
            int x = Integer.parseInt(sp[0]);
            int y = Integer.parseInt(sp[1]);
            int dirIdx = indexOf(dirs, sp[2]);
            // validate start
            if (!inBounds(x, y, board) || hasObstacle(board, x, y)) {
                print(mapper, new Result(-1, -1, dirs[dirIdx], "INVALID_START_POSITION"));
                return;
            }
            // process commands
            for (int i = 1; i < commands.size(); i++) {
                String cmd = commands.get(i);
                String[] parts = cmd.split(" ");
                switch (parts[0]) {
                    case "ROTATE" -> dirIdx = indexOf(dirs, parts[1]);
                    case "MOVE" -> {
                        int steps = Integer.parseInt(parts[1]);
                        for (int s = 0; s < steps; s++) {
                            Point mv = moves.get(dirs[dirIdx]);
                            int nx = x + mv.x, ny = y + mv.y;
                            if (!inBounds(nx, ny, board)) {
                                print(mapper, new Result(x, y, dirs[dirIdx], "OUT_OF_THE_BOARD"));
                                return;
                            }
                            if (hasObstacle(board, nx, ny)) break;
                            x = nx; y = ny;
                        }
                    }
                    default -> { /* ignore */ }
                }
            }
            // success
            print(mapper, new Result(x, y, dirs[dirIdx], "SUCCESS"));
        } catch (Exception e) {
            // output generic error JSON
            try { System.out.println(mapper.writeValueAsString(new Result("GENERIC_ERROR"))); }
            catch (JsonProcessingException ignored) {}
            System.exit(0);
        }
    }

    private static boolean inBounds(int x, int y, Board board) {
        return x >= 0 && x < board.width && y >= 0 && y < board.height;
    }

    private static boolean hasObstacle(Board board, int x, int y) {
        for (Point o : board.obstacles) {
            if (o.x == x && o.y == y) return true;
        }
        return false;
    }

    private static int indexOf(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(val)) return i;
        }
        return -1;
    }

    private static void print(ObjectMapper mapper, Result res) {
        try { System.out.println(mapper.writeValueAsString(res)); }
        catch (JsonProcessingException e) { throw new RuntimeException(e); }
    }

    private static Point createPoint(int x, int y) {
        Point p = new Point(); p.x = x; p.y = y; return p;
    }

    private static <T> T fetchJson(String urlStr, TypeReference<T> type) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() != 200) throw new RuntimeException("HTTP code: " + conn.getResponseCode());

        try (InputStream in = conn.getInputStream()) {
            return new ObjectMapper().readValue(in, type);
        }
    }
}
