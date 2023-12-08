package io.github.jdouglas9025.socialmediaapi.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class S3Service {
    private static final Regions region = Regions.US_EAST_2;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    @Value("${spring.aws.awsAccessKey}")
    private static String awsAccessKey;
    @Value("${spring.aws.awsSecretKey}")
    private static String awsSecretKey;
    private static final AWSCredentials credentials = new BasicAWSCredentials(
            awsAccessKey,
            awsSecretKey
    );
    private static final AmazonS3 client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build();
    @Value("${spring.aws.bucketName}")
    private static String bucket;
    @Value("${spring.aws.baseUrl}")
    private static String baseUrl;

    /**
     * Uploads the specified file (e.g., image) to the standard AWS S3 bucket
     *
     * @param file the file to upload
     * @return a URI reference to the object in S3
     */
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

    /**
     * Utility method for getting an input stream from a Flux of type DataBuffer
     *
     * @param data the Flux of type DataBuffer to create an input stream from
     * @return an input stream based off the supplied argument
     * @throws IOException if an IO exception is encountered during processing
     */
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

    /**
     * Deletes the specified object from the standard AWS S3 bucket
     *
     * @param reference a URI reference to the object in S3
     */
    public static void deleteObject(String reference) {
        try {
            String key = Path.of(reference).getFileName().toString();

            //Delete object from bucket
            DeleteObjectRequest deletion = new DeleteObjectRequest(bucket, key);
            client.deleteObject(deletion);
        } catch (SdkClientException ignored) {
        }
    }
}
