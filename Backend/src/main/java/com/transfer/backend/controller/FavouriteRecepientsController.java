package com.transfer.backend.controller;

import com.transfer.backend.dto.CreateFavoriteRecipientReq;
import com.transfer.backend.dto.CreateFavoriteRecipientResp;
import com.transfer.backend.dto.*;
import com.transfer.backend.service.FavoriteRecipientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class FavouriteRecepientsController {
    @Autowired
    private FavoriteRecipientService favoriteRecipientService;

    @PostMapping("/{userId}/favorites")
    public ResponseEntity<CreateFavoriteRecipientResp> addFavoriteRecipient(@PathVariable Long userId, @RequestBody CreateFavoriteRecipientReq req) {
        CreateFavoriteRecipientResp resp = favoriteRecipientService.addFavoriteRecipient(req, userId);
        return ResponseEntity.ok(resp);
    }
///////////////////////////////////////////////////////
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<CreateFavoriteRecipientResp>> getFavoriteRecipients(@PathVariable Long userId) {
        List<CreateFavoriteRecipientResp> recipients = favoriteRecipientService.getFavoriteRecipients(userId);
        return ResponseEntity.ok(recipients);
    }

    @DeleteMapping("/{userId}/favorites/{favoriteId}")
    public ResponseEntity<?> removeFavoriteRecipient(@PathVariable Long userId, @PathVariable Long favoriteId) {
        favoriteRecipientService.removeFavoriteRecipient(favoriteId);
        return ResponseEntity.ok("Favorite recipient removed successfully");
    }
}
