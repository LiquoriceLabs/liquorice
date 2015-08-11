package io.liquorice.config.core.cache.file;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import io.liquorice.config.core.cache.CacheLayer;
import io.liquorice.config.core.cache.NullIterator;
import io.liquorice.config.core.logging.Log;
import io.liquorice.config.core.logging.LogFactory;

/**
 * Created by mthorpe on 6/10/15.
 */
public class SinglePropertiesFileCache extends AbstractFileCache implements CacheLayer {
    private static final Log LOG = LogFactory.getLog(SinglePropertiesFileCache.class);

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        try {
            getCacheReader().reset();

            return new Iterator<Map.Entry<String, Object>>() {
                String bufferedLine;

                @Override
                public boolean hasNext() {
                    try {
                        do {
                            bufferedLine = getCacheReader().readLine();
                        } while (bufferedLine != null && (bufferedLine.startsWith("#") || !bufferedLine.contains("=")));
                        return bufferedLine != null;
                    } catch (IOException e) {
                        return false;
                    }
                }

                @Override
                public Map.Entry<String, Object> next() throws NoSuchElementException {
                    if (bufferedLine == null && !hasNext()) {
                        throw new NoSuchElementException();
                    }

                    int idx = bufferedLine.indexOf('=');
                    Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<String, Object>(
                            bufferedLine.substring(0, idx), bufferedLine.substring(idx + 1));
                    bufferedLine = null;
                    return entry;
                }

                @Override
                public void remove() {
                    // Unsupported
                }
            };
        } catch (IOException e) {
            LOG.warning(e);
            return new NullIterator();
        }
    }
}
