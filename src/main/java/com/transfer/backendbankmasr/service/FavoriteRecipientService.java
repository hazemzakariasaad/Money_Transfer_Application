// Backend/src/main/java/com/transfer/backendbankmasr/service/FavoriteRecipientService.java
package com.transfer.backendbankmasr.service;

import com.transfer.backendbankmasr.dto.CreateFavoriteRecipientReq;
import com.transfer.backendbankmasr.dto.CreateFavoriteRecipientResp;
import com.transfer.backendbankmasr.entity.FavoriteRecipient;
import com.transfer.backendbankmasr.entity.User;
import com.transfer.backendbankmasr.repository.FavoriteRecipientRepository;
import com.transfer.backendbankmasr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteRecipientService {

    @Autowired
    private FavoriteRecipientRepository favoriteRecipientRepository;

    @Autowired
    private UserRepository userRepository;

    public CreateFavoriteRecipientResp addFavoriteRecipient(CreateFavoriteRecipientReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FavoriteRecipient favoriteRecipient = new FavoriteRecipient();
        favoriteRecipient.setUser(user);
        favoriteRecipient.setRecipientName(req.getRecipientName());
        favoriteRecipient.setRecipientAccountNumber(req.getRecipientAccountNumber());

        FavoriteRecipient savedRecipient = favoriteRecipientRepository.save(favoriteRecipient);
        return new CreateFavoriteRecipientResp(savedRecipient.getId(), savedRecipient.getRecipientName(), savedRecipient.getRecipientAccountNumber());
    }

    public List<CreateFavoriteRecipientResp> getFavoriteRecipients(Long userId) {
        return favoriteRecipientRepository.findByUser_UserId(userId).stream()
                .map(recipient -> new CreateFavoriteRecipientResp(recipient.getId(), recipient.getRecipientName(), recipient.getRecipientAccountNumber()))
                .collect(Collectors.toList());
    }

    public void removeFavoriteRecipient(Long favoriteId) {
        favoriteRecipientRepository.deleteById(favoriteId);
    }
}
