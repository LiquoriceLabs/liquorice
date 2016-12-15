package io.liquorice.config.formatter.json.gson;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.liquorice.config.api.formatter.StreamableConfigFormatter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation of a {@link StreamableConfigFormatter} where the values are interpreted as JSON strings and the
 * implementation is based around Google/Gson
 */
public class GsonConfigFormatter implements StreamableConfigFormatter {

    private Gson gson;

    /**
     * CTOR
     */
    private GsonConfigFormatter(final Builder builder) {
        this.gson = builder.gsonBuilder.create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final Object value, final Class<T> valueType) {
        if (value instanceof InputStream) {
            return read((InputStream) value, valueType);
        } else if (value instanceof Reader) {
            return read((Reader) value, valueType);
        } else if (value instanceof String) {
            return read((String) value, valueType);
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final Reader reader, final Class<T> valueType) {
        try {
            return Optional.ofNullable(gson.fromJson(reader, valueType));
        } catch (final JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final InputStream inputStream, final Class<T> valueType) {
        return read(new InputStreamReader(inputStream, Charsets.UTF_8), valueType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final String string, final Class<T> valueType) {
        try {
            return Optional.ofNullable(gson.fromJson(string, valueType));
        } catch (final JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object write(final Object value) {
        return gson.toJson(value);
    }

    /**
     * Builder
     */
    public static final class Builder {

        private GsonBuilder gsonBuilder;

        /**
         * CTOR
         */
        public Builder() {
            this.gsonBuilder = new GsonBuilder();
        }

        /**
         * Overwrite the default {@link GsonBuilder} with a custom one
         *
         * @param gsonBuilder
         *            the {@link GsonBuilder}
         * @return this
         */
        public Builder withGsonBuilder(final GsonBuilder gsonBuilder) {
            this.gsonBuilder = checkNotNull(gsonBuilder);
            return this;
        }

        /**
         * Register a {@link com.google.gson.TypeAdapter} with the underlying {@link GsonBuilder}
         * 
         * @param type
         *            the type
         * @param typeAdapter
         *            the type adapter
         * @return this
         */
        public Builder withTypeAdapter(final Type type, final Object typeAdapter) {
            this.gsonBuilder.registerTypeAdapter(checkNotNull(type), checkNotNull(typeAdapter));
            return this;
        }

        /**
         * Build
         * 
         * @return a new {@link GsonConfigFormatter} built to specification
         */
        public GsonConfigFormatter build() {
            return new GsonConfigFormatter(this);
        }
    }
}
