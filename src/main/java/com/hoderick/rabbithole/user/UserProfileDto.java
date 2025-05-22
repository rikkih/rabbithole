package com.hoderick.rabbithole.user;

public record UserProfileDto(String name, String email, String bio, String avatarUrl) {
}
