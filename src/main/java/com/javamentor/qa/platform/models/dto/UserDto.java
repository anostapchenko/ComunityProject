package com.javamentor.qa.platform.models.dto;

public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String linkImage;
    private String city;
    private int reputation;

    public UserDto(Long id, String email, String fullName, String linkImage, String city, int reputation) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.linkImage = linkImage;
        this.city = city;
        this.reputation = reputation;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public String getCity() {
        return city;
    }

    public int getReputation() {
        return reputation;
    }
}
