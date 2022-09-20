package org.octobot.bot.game.loader;

import org.octobot.bot.Environment;
import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.client.RSClient;
import org.octobot.bot.game.loader.internal.Injector;
import org.octobot.bot.handler.TextHandler;
import org.octobot.bot.internal.Proxy;
import org.octobot.bot.internal.ui.component.ClientStub;

import java.applet.Applet;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Crawler
 *
 * @author Pat-ji
 */
public class Crawler {
    public static int world = 2;

    private String page;
    public boolean error;

    private boolean loadGamePage(final int world) throws Exception {
        if (page == null) {
            final HttpURLConnection connection = HttpClient.getCachedConnection(new URL("http://oldschool" + world + ".runescape.com/j1"));
            if (connection == null) return false;

            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            final StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null)
                builder.append(line).append("\n");

            final String text = builder.toString();
            page = text.substring(0, text.length() - 1);
            reader.close();
        }

        return page != null;
    }

    private String getJar(final int world) {
        final Matcher matcher = Pattern.compile("document.write\\('archive=(.*?) '\\);").matcher(page);
        return matcher.find() ? "http://oldschool" + world + ".runescape.com/" + matcher.group(1) : null;
    }

    private Map<String, String> getPramaters() {
        final Map<String, String> parameters = new HashMap<String, String>();
        final Matcher matcher = Pattern.compile("<param name=\"(.*?)\" value=\"(.*?)\">").matcher(page);
        while (matcher.find())
            parameters.put(matcher.group(1), matcher.group(2));

        return parameters;
    }

    private void saveClient(final Map<String, byte[]> data) throws Exception {
        final JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(Environment.getLibraryDirectory() + File.separator + "game.jar"));
        for (final String index : data.keySet()) {
            outputStream.putNextEntry(new JarEntry(index + ".class"));
            outputStream.write(data.get(index));
            outputStream.closeEntry();
        }

        outputStream.close();
        System.out.println("- A new client has been stored.");
    }

    private void setProxy(final Proxy proxy) {
        System.setProperty("http.proxyHost", TextHandler.decode(proxy.host));
        System.setProperty("http.proxyPort", TextHandler.decode(proxy.port));

        if (proxy.username != null)
            System.setProperty("http.proxyUser", TextHandler.decode(proxy.username));

        if (proxy.password != null)
            System.setProperty("http.proxyPassword", TextHandler.decode(proxy.password));
    }

    public Applet loadApplet(final GameDefinition definition, final boolean restore) throws Exception {
        error = false;

        System.out.println("^ Crawler started for bot " + definition.index + " ^");
        final long time = System.currentTimeMillis();

        if (definition.proxy != null) {
            setProxy(definition.proxy);
        } else if (Environment.proxy != null) {
            setProxy(Environment.proxy);
        }

        if (!loadGamePage(world)) {
            System.out.println("- Failed to load game page data.");
            return null;
        }

        final String jar = getJar(world);
        final Map<String, String> parameters = getPramaters();
        final Map<String, byte[]> classes = new HashMap<String, byte[]>();

        byte[] data;
        final File file = new File(Environment.getLibraryDirectory() + File.separator + "game.jar");
        if (file.exists() && !restore) {
            System.out.println("- Loading stored client.");
            data = readInputSteam(new FileInputStream(file.getAbsolutePath()));
        } else {
            System.out.println("- Downloading and loading the new client.");
            data = readInputSteam(new URL(jar).openStream());
        }

        if (data == null)
            throw new NullPointerException("- Failed to read jar input stream.");

        System.out.println("- Successfully loaded the game data, storing classes.");

        final JarStream jarStream = new JarStream(new ByteArrayInputStream(data));
        JarEntry entry;
        while ((entry = jarStream.getNextJarEntry()) != null) {
            String entryName = entry.getName();
            if (entryName.endsWith(".class")) {
                entryName = entryName.replace('/', '.');
                final String name = entryName.substring(0, entryName.length() - 6);
                if (!classes.containsKey(name))
                    classes.put(name, jarStream.readStream());
            }
        }

        jarStream.close();

        if (!file.exists() || restore)
            saveClient(classes);

        System.out.println("- Successfully stored the game data, injecting classes.");
        definition.classLoader = new GameClassLoader(this, new Injector(classes).inject(classes));

        if (error) {
            System.out.println("- Error whilst injecting, restarting.");
            return loadApplet(definition, true);
        }

        System.out.println("- Initializing client.");
        final Class<?> client = definition.classLoader.loadClass("client");
        if (client == null) {
            System.out.println("- Failed to load client class.");

            if (error) {
                System.out.println("- Error whilst injecting, restarting.");
                return loadApplet(definition, true);
            }

            return null;
        }

        try {
            for (final Field field : client.getDeclaredFields())
                if (field.getName().equals("game")) {
                    field.set(null, definition);
                    break;
                }
        } catch (final Error e) {
            System.out.println("- Critical error, retrying.");
            e.printStackTrace();
            return loadApplet(definition, true);
        }

        final Object clientInstance = client.newInstance();
        definition.client = (RSClient) clientInstance;

        System.out.println("- Constructing Applet.");
        final Applet applet = (Applet) clientInstance;
        applet.setStub(new ClientStub(new URL(jar), parameters));
        applet.init();
        applet.start();
        applet.setSize(765, 503);

        System.out.println("- Game successfully loaded in " + (System.currentTimeMillis() - time) + "ms. -\n");
        return applet;
    }

    public byte[] readInputSteam(final InputStream stream) throws Exception {
        if (stream == null) return null;

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final InputStream is = new BufferedInputStream(stream);

        int read;
        final byte[] buff = new byte[2048];
        while ((read = is.read(buff)) != -1) {
            bos.write(buff, 0, read);
        }

        is.close();
        return bos.toByteArray();
    }

}
