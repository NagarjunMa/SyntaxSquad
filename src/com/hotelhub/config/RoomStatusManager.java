package com.hotelhub.config;

import java.util.HashMap;
import java.util.Map;

public class RoomStatusManager {
    private Map<Integer, String> roomStatusMap = new HashMap<>();

    public void updateRoomStatus(int roomId, String status) {
        roomStatusMap.put(roomId, status);
    }

    public String getRoomStatus(int roomId) {
        return roomStatusMap.get(roomId);
    }

    public Map<Integer, String> getAllStatuses() {
        return roomStatusMap;
    }
}

