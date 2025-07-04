package net.catsnap.domain.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feed_photo")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    private String photo_url;

    public FeedPhoto(Feed feed, String photoUrl) {
        this.feed = feed;
        this.photo_url = photoUrl;
    }

    public String toPhotoUrl() {
        return photo_url;
    }
}
