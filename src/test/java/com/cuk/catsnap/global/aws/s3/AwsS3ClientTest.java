package com.cuk.catsnap.global.aws.s3;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AwsS3ClientTest {

    @InjectMocks
    private AwsS3Client awsS3Client;

    @Mock
    private AmazonS3 amazonS3;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        FieldUtils.writeField(awsS3Client, "expirationTime", 1000, true);
        FieldUtils.writeField(awsS3Client, "bucketNameRoot", "bucket-name", true);
        FieldUtils.writeField(awsS3Client, "bucketNameRaw", "raw-bucket", true);
    }

    @Test
    void 사진을_업로드_할_수_있는_URL을_받아온다() throws MalformedURLException {
        //given
        given(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
            .willReturn(new URL("http://test.com"));

        //when
        URL url = awsS3Client.getUploadImageUrl("test.jpg");

        //then
        Assertions.assertThat(url).isEqualTo(new URL("http://test.com"));
    }
}