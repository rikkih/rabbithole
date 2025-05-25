package com.hoderick.rabbithole.user.dto;

public record UserProfileDto(String id, String name, String email, String bio, String avatarUrl) {
}
