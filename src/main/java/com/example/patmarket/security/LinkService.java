package com.example.patmarket.security;

import com.example.patmarket.model.Role;
import com.example.patmarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinkService {

    @Autowired
    private UserRepository userRepository;
    public List<LinkDto> getLinkList() {

        List<LinkDto> linkDtos = new ArrayList<>();
        linkDtos.add(new LinkDto("главная страница","/main"));
        linkDtos.add(new LinkDto("вход","/auth/login"));
        if (isAdmin()) {
            linkDtos.add(new LinkDto("продукты","/products"));
            linkDtos.add(new LinkDto("категории","/categories"));
            linkDtos.add(new LinkDto("питомцы","/animal/find-all"));
            linkDtos.add(new LinkDto("заказы","/order/admin/find-all"));
        } else {
            linkDtos.add(new LinkDto("корзина","/order/" + getCurrentUserId()));
        }
       return linkDtos;
    }

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public long getCurrentUserId() {
        return userRepository.findByEmail(getCurrentUsername())
                .orElseThrow()
                .getUserId();
    }

    private boolean isAdmin() {
        return userRepository.findByEmail(getCurrentUsername()).orElseThrow().getRole().equals(Role.ADMIN);
    }
}
