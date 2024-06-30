package prateek_gupta.sample_project.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class BeanConfiguration {
    @Value("${AWS_ACCESS_KEY}")
    String AWS_ACCESS_KEY="";

    @Value("${AWS_SECRET_KEY}")
    String AWS_SECRET_KEY="";
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(AWS_ACCESS_KEY,AWS_SECRET_KEY)))
                .build();
    }
}
