package com.github.bproenca.aibcp.config;

//public class ChatClientConfig {}

import com.oracle.bmc.ClientConfiguration;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.retrier.RetryConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ChatClientConfig {

    /*
spring.ai.oci.genai.authenticationType=file
spring.ai.oci.genai.file=/home/bcp/.oci/config
spring.ai.oci.genai.cohere.chat.options.compartment=ocid1.compartment.oc1..aaaaaaaagh2se2x7paw2mdf3vxc42e5hzm22v3a735hhypoxxftcigm2alvq
spring.ai.oci.genai.cohere.chat.options.servingMode=on-demand
spring.ai.oci.genai.cohere.chat.options.model=ocid1.generativeaimodel.oc1.sa-saopaulo-1.amaaaaaask7dceyax3s2zv7jbmglo2eriechqdkhre2mxh54io6f57op35la

spring.ai.oci.genai.authenticationType=file
spring.ai.oci.genai.file=/home/bcp/.oci/config
spring.ai.oci.genai.cohere.chat.options.servingMode=on-demand
spring.ai.oci.genai.cohere.chat.options.model=ocid1.generativeaimodel.oc1.sa-saopaulo-1.amaaaaaask7dceyaxu7lvx6k45r2hapxtuc2q5rleaujcowq6xbcywwtzhsq
spring.ai.oci.genai.cohere.chat.options.compartment=ocid1.compartment.oc1..aaaaaaaagh2se2x7paw2mdf3vxc42e5hzm22v3a735hhypoxxftcigm2alvq
     */

    @Value( "${spring.ai.oci.genai.file}" )
    private String CONFIG_LOCATION = "~/.oci/config";
    @Value( "${spring.ai.oci.genai.profile}" )
    private String CONFIG_PROFILE = "DEFAULT";
    @Value( "${spring.ai.oci.genai.endpoint}" )
    private String ENDPOINT = "https://inference.generativeai.sa-saopaulo-1.oci.oraclecloud.com";
    @Value( "${spring.ai.oci.genai.region}" )
    private String REGION = "sa-saopaulo-1";

    @Bean
    public GenerativeAiInferenceClient chatClient() throws IOException {

        // Configuring the AuthenticationDetailsProvider. It's assuming there is a default OCI config file
        // "~/.oci/config", and a profile in that config with the name defined in CONFIG_PROFILE variable.
        final ConfigFileReader.ConfigFile configFile =  ConfigFileReader.parse(CONFIG_LOCATION, CONFIG_PROFILE);
        final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

        // Set up Generative AI client with credentials and endpoint
        ClientConfiguration clientConfiguration =
                ClientConfiguration.builder()
                        .readTimeoutMillis(240000)
                        .retryConfiguration(RetryConfiguration.NO_RETRY_CONFIGURATION)
                        .build();

        final GenerativeAiInferenceClient generativeAiInferenceClient  = GenerativeAiInferenceClient.builder().configuration(clientConfiguration).build(provider);
        generativeAiInferenceClient.setEndpoint(ENDPOINT);
        generativeAiInferenceClient.setRegion(REGION);

        return generativeAiInferenceClient;
    }
}