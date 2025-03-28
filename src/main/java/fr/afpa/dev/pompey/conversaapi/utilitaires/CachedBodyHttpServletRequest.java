package fr.afpa.dev.pompey.conversaapi.utilitaires;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import fr.afpa.dev.pompey.conversaapi.utilitaires.DelegatingServletInputStream;

/**
 * Classe pour la requête HTTP avec le corps en cache.
 */
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private final String body;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        body = stringBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        return new DelegatingServletInputStream(byteArrayInputStream);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    public String getBody() {
        return body;
    }
}
