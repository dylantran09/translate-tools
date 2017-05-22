package core.base;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.util.Constant;

@SuppressWarnings("unused")
public abstract class BaseMultipartParam implements Param {

    /**
     * The multipart form data header
     */
    private static final String MULTIPART_FORM_DATA = "multipart/form-data;boundary=";

    /**
     * The break line for multipart header
     */
    private static final String BREAK_LINE = "\r\n";

    /**
     * The hyphens for multipart header
     */
    private static final String HYPHENS = "--";

    /**
     * The content of multipart header
     */
    private static final String CONTENT = "%s%s%sContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"%s Content-Type: %s %s Content-Transfer-Encoding: binary %s%s";

    /**
     * The set of key-value headers for the webservice message
     */
    private final HashMap<String, String> headers;

    /**
     * The set of key-value text parts
     */
    private final HashMap<String, List<Text>> texts;

    /**
     * The default value of charset
     */
    private final String CHARSET = "UTF-8";

    /**
     * The set of key-value file parts
     */
    private final HashMap<String, List<Binary>> binaries;

    /**
     * The boundary string for the request
     */
    private String boundary = "*****";

    public BaseMultipartParam() {
        this.headers = new HashMap<>();
        this.texts = new HashMap<>();
        this.binaries = new HashMap<>();
    }

    public final BaseMultipartParam addTextPart(String key, String value) {
        List<Text> values;
        if (texts.containsKey(key)) {
            values = texts.get(key);
        } else {
            values = new ArrayList<>();
        }
        values.add(new Text(value, Charset.forName(CHARSET)));
        texts.put(key, values);
        return this;
    }

    public final BaseMultipartParam addTextPart(String key, String value, Charset charSet) {
        List<Text> values;
        if (texts.containsKey(key)) {
            values = texts.get(key);
        } else {
            values = new ArrayList<>();
        }
        values.add(new Text(value, charSet));
        texts.put(key, values);
        return this;
    }

    public final BaseMultipartParam addBinaryPart(String key, String name, String type, byte[] value) {
        List<Binary> values;
        if (binaries.containsKey(key)) {
            values = binaries.get(key);
        } else {
            values = new ArrayList<>();
        }
        values.add(new Binary(name, type, value));
        binaries.put(key, values);
        return this;
    }

    /**
     * @return the boundary
     */
    public final String getBoundary() {
        return boundary;
    }

    /**
     * @param boundary the boundary to set
     */
    public final void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    @Override
    public final byte[] makeRequestBody() {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setBoundary(boundary);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (String key : texts.keySet()) {
            List<Text> textValues = texts.get(key);
            for (Text text : textValues) {
                builder.addPart(key, new StringBody(text.getContent(),
                        ContentType.MULTIPART_FORM_DATA.withCharset(text.getCharset())));
            }
        }
        for (String key : binaries.keySet()) {
            List<Binary> binaryValues = binaries.get(key);
            for (Binary binary : binaryValues) {
                builder.addBinaryBody(key, binary.getContent(), ContentType.create(binary.getType()), binary.getName());
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            builder.build().writeTo(os);
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public HashMap<String, String> makeRequestHeaders() {
        headers.put(Constant.Header.CONTENT_TYPE.toString(),
                MULTIPART_FORM_DATA + boundary);
        return headers;
    }

    @Override
    public String makeBodyContentType() {
        return MULTIPART_FORM_DATA;
    }

    private class Text {
        private String content;
        private Charset charset;

        public Text(String content, Charset charset) {
            super();
            this.content = content;
            this.charset = charset;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Charset getCharset() {
            return charset;
        }

        public void setCharset(Charset charset) {
            this.charset = charset;
        }
    }

    private class Binary {
        private final String name;
        private final String type;
        private final byte[] content;

        public Binary(String name, String type, byte[] content) {
            super();
            this.name = name;
            this.type = type;
            this.content = content;
        }

        public String getName() {
            return name;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @return the content
         */
        public byte[] getContent() {
            return content;
        }
    }
}
