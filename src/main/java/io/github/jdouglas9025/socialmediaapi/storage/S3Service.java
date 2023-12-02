package io.github.jdouglas9025.socialmediaapi.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class S3Service {
    private static final AWSCredentials credentials = new BasicAWSCredentials(
            System.getenv("awsAccessKey"),
            System.getenv("awsSecretKey")
    );
    private static final Regions region = Regions.US_EAST_2;
    private static final String bucket = "doggo-life-posts-images-bucket";
    private static final AmazonS3 client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build();
    private static final String baseUrl = "https://doggo-life-posts-images-bucket.s3.us-east-2.amazonaws.com/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

    public static String uploadObject(FilePart file) {
        try {
            //Set key based on timestamp + file name
            String timestamp = LocalDateTime.now().format(formatter) + "-";
            String key = timestamp + Path.of(Objects.requireNonNull(file.filename()))
                    .getFileName()
                    .toString();

            InputStream data = getInputStreamFromFluxDataBuffer(file.content());

            //Upload file as new object to S3 images bucket (can also specify optional metadata)
            PutObjectRequest upload = new PutObjectRequest(bucket, key, data, new ObjectMetadata());
            client.putObject(upload);

            //Return reference to image in S3
            return baseUrl + key;
        } catch (SdkClientException | IOException e) {
            return null;
        }
    }

    private static InputStream getInputStreamFromFluxDataBuffer(Flux<DataBuffer> data) throws IOException {
        PipedOutputStream osPipe = new PipedOutputStream();
        PipedInputStream isPipe = new PipedInputStream(osPipe);

        DataBufferUtils.write(data, osPipe)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete(() -> {
                    try {
                        osPipe.close();
                    } catch (IOException ignored) {
                    }
                })
                .subscribe(DataBufferUtils.releaseConsumer());

        return isPipe;
    }

    public static File downloadObject(String imageRef) {
        try {
            String key = Path.of(imageRef).getFileName().toString();

            //Download object back as byte array
            GetObjectRequest download = new GetObjectRequest(bucket, key);
            byte[] data = client.getObject(download).getObjectContent().readAllBytes();

            //Write contents to file with key as file name
            File file = new File(key);
            OutputStream stream = new FileOutputStream(file);

            stream.write(data);
            stream.close();

            return file;
        } catch (SdkClientException | IOException e) {
            return null;
        }
    }
}
