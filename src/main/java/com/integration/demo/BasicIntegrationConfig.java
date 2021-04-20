package com.integration.demo;

import java.io.File;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.annotation.Poller;
/*
 * The following code demonstrates the basic functionality of an FTP integration.
 * Steps:
 * 
 * We will declare a message channel
 * We will create an adapter that polls for a file based on a pattern (Poll every 1000 ms)
 * We will then place the file/message on a channel
 * We will then consume the message off of the channel and FTP to an output directory and
 * delete the file from source.
 * 
 * 
 */
@Configuration
@EnableIntegration
public class BasicIntegrationConfig{
    public String INPUT_DIR = "C:\\Users\\username\\Desktop\\SpringINTG\\source";
    public String OUTPUT_DIR = "C:\\Users\\username\\Desktop\\SpringINTG\\target";
    public String FILE_PATTERN = "*.mkv";
    
    /*
     * Define the behaviour of the channel defined here based on the method name:
     * eg:
     * P2P 		- Point-to-Point
     * Pub-Sub	- Publish-Subscribe
     * 
     * In this case we have defined the channel called fileChannel to be a P2P channel.
     * This means that there can only be a single consumer for set channel which has been
     * defined to be the fileWritingMessageHandler method in this case.
     */
    @Bean
    public MessageChannel fileChannel() {
        return new DirectChannel();
    }
    
   
    /*
     * Inbound adapters, as we have seen, are used to bring in messages 
     * from the external system (in this case a filesystem directory).
     */
    @Bean
    @InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource sourceReader= new FileReadingMessageSource();
        sourceReader.setDirectory(new File(INPUT_DIR));
        sourceReader.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));
        return sourceReader;
    }
    
    /*
     * Outbound adapters are used to send messages outwards. Spring Integration 
     * supports a large variety of out-of-the-box adapters for various common use cases.
     */
    @Bean
    @ServiceActivator(inputChannel= "fileChannel")
    public MessageHandler fileWritingMessageHandler() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR));
        handler.setDeleteSourceFiles(true);
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        return handler;
    }
    
    
    /*
     * The org.springframework.integration.Message interface defines the spring Message: 
     * the unit of data transfer within a Spring Integration context.
     */
    
    /*
    public interface Message<T> {
        T getPayload();
        MessageHeaders getHeaders();
    }
	*/
}
