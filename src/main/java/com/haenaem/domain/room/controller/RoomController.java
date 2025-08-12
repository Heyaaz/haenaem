package com.haenaem.domain.room.controller;

import com.haenaem.domain.room.dto.ItemPlacementRequest;
import com.haenaem.domain.room.dto.RoomDto;
import com.haenaem.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;

    /**
     * 사용자 방 조회
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<RoomDto> getUserRoom(@PathVariable Long userId) {
        RoomDto room = roomService.getUserRoom(userId);
        return ResponseEntity.ok(room);
    }

    /**
     * 아이템 배치/위치 이동
     */
    @PostMapping("/users/{userId}/items/place")
    public ResponseEntity<Void> placeItem(
        @PathVariable Long userId,
        @RequestBody ItemPlacementRequest request) {
        roomService.placeItem(userId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 아이템 배치 해제
     */
    @DeleteMapping("/users/{userId}/items/{inventoryItemId}")
    public ResponseEntity<Void> removeItem(
        @PathVariable Long userId,
        @PathVariable Long inventoryItemId) {
        roomService.removeItem(userId, inventoryItemId);
        return ResponseEntity.ok().build();
    }
}