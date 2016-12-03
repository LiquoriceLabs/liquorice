package io.liquorice.config.service;

import io.liquorice.config.exception.ConfigurationException;
import io.liquorice.config.utils.VariableUtilities;
import io.liquorice.config.utils.logging.Log;
import io.liquorice.config.utils.logging.LogFactory;

import java.io.File;
import java.util.Arrays;

/**
 * Created by mthorpe on 4/26/15.
 * <p/>
 * Starting point for Liquorice
 */
public class LiquoriceService {
    public static final String ENV_DATA_DIRECTORY = "LIQUORICE_DATA";
    private static final String DEFAULT_DATA_DIRECTORY = "./data";
    private static final Log LOG = LogFactory.getLog(LiquoriceService.class);

    private String dataDirectory = null;

    /**
     * Entry point into application.
     *
     * @param args
     *            passed onto {@link #run(String[])}
     */
    public static void main(String[] args) {
        try {
            new LiquoriceService().setDataDirectory(LiquoriceService.ENV_DATA_DIRECTORY).run(
                    Arrays.copyOf(args, args.length));
        } catch (ConfigurationException e) {
            LOG.severe(e);
            System.exit(1);
        }
    }

    private boolean checkConfig() {
        if (dataDirectory == null) {
            dataDirectory = DEFAULT_DATA_DIRECTORY;
        }
        return new File(dataDirectory).mkdirs();
    }

    /**
     * The "do stuff method"
     *
     * @param args
     *            Arguments passed to the service
     * @throws ConfigurationException
     *             if the supplied configuration is invalid
     */
    public void run(String[] args) throws ConfigurationException {
        if (!checkConfig()) {
            throw new ConfigurationException("Failed to locate or initialize data directory [" + dataDirectory + "]");
        }
    }

    /**
     * Sets the directory where data should be stored to and retrieved from
     *
     * @param envVar
     *            The name of an environment variable storing the
     * @return this
     */
    public LiquoriceService setDataDirectory(String envVar) {
        dataDirectory = VariableUtilities.getFromEnv(envVar, DEFAULT_DATA_DIRECTORY);
        return this;
    }

    /**
     * Sets the directory where data should be stored to and retrieved from
     *
     * @param path
     *            The directory
     * @return this
     */
    public LiquoriceService setDataDirectory(File path) {
        this.dataDirectory = VariableUtilities.getOrSetDefault(path.getAbsolutePath(), DEFAULT_DATA_DIRECTORY);
        return this;
    }
}