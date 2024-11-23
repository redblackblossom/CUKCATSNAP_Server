package com.cuk.catsnap.support.fixture;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.review.entity.Review;
import com.cuk.catsnap.domain.review.entity.ReviewLike;

public class ReviewLikeFixture {

    private Long id;
    private Review review = ReviewFixture.review().build();
    private Member member = MemberFixture.member().build();
    private Photographer photographer = PhotographerFixture.photographer().build();
    private Boolean liked = true;

    public static ReviewLikeFixture reviewLike() {
        return new ReviewLikeFixture();
    }

    public ReviewLikeFixture id(Long id) {
        this.id = id;
        return this;
    }

    public ReviewLikeFixture member(Member member) {
        this.member = member;
        return this;
    }

    public ReviewLikeFixture photographer(Photographer photographer) {
        this.photographer = photographer;
        return this;
    }

    public ReviewLikeFixture liked(Boolean liked) {
        this.liked = liked;
        return this;
    }

    public ReviewLike build() {
        return ReviewLike.builder()
            .id(id)
            .review(review)
            .member(member)
            .photographer(photographer)
            .liked(liked)
            .build();
    }
}