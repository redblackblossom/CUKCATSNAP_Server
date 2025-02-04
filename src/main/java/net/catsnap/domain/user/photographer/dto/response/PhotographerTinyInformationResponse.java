package net.catsnap.domain.user.photographer.dto.response;

import net.catsnap.domain.user.photographer.entity.Photographer;

public record PhotographerTinyInformationResponse(
    Long photographerId,
    String nickname,
    String profilePhotoUrl
) {

    public static PhotographerTinyInformationResponse from(Photographer photographer) {
        return new PhotographerTinyInformationResponse(photographer.getId(),
            photographer.getNickname(),
            photographer.getProfilePhotoUrl());
    }
}
