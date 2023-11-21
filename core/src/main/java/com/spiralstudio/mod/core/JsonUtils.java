package com.spiralstudio.mod.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Leego Yih
 */
public final class JsonUtils {
    private static final JsonMapper mapper;

    static {
        mapper = JsonMapper.builder()
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
    }

    public static String toString(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String json, Class<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String json, JavaType type) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String json, TypeReference<T> ref) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(byte[] bytes, Class<T> type) {
        if (bytes == null) {
            return null;
        }
        try {
            return mapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(byte[] bytes, JavaType type) {
        if (bytes == null) {
            return null;
        }
        try {
            return mapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(byte[] bytes, TypeReference<T> ref) {
        if (bytes == null) {
            return null;
        }
        try {
            return mapper.readValue(bytes, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(InputStream inputStream, Class<T> type) {
        if (inputStream == null) {
            return null;
        }
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(InputStream inputStream, JavaType type) {
        if (inputStream == null) {
            return null;
        }
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(InputStream inputStream, TypeReference<T> ref) {
        if (inputStream == null) {
            return null;
        }
        try {
            return mapper.readValue(inputStream, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E> List<E> parseList(String json, Class<E> type) {
        return parse(json, mapper.getTypeFactory().constructParametricType(List.class, type));
    }

    public static <E> List<E> parseList(byte[] bytes, Class<E> type) {
        return parse(bytes, mapper.getTypeFactory().constructParametricType(List.class, type));
    }

    public static <E> List<E> parseList(InputStream inputStream, Class<E> type) {
        return parse(inputStream, mapper.getTypeFactory().constructParametricType(List.class, type));
    }

    public static <E> Set<E> parseSet(String json, Class<E> type) {
        return parse(json, mapper.getTypeFactory().constructParametricType(Set.class, type));
    }

    public static <E> Set<E> parseSet(byte[] bytes, Class<E> type) {
        return parse(bytes, mapper.getTypeFactory().constructParametricType(Set.class, type));
    }

    public static <E> Set<E> parseSet(InputStream inputStream, Class<E> type) {
        return parse(inputStream, mapper.getTypeFactory().constructParametricType(Set.class, type));
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> keyType, Class<V> valueType) {
        return parse(json, mapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType));
    }

    public static <K, V> Map<K, V> parseMap(byte[] bytes, Class<K> keyType, Class<V> valueType) {
        return parse(bytes, mapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType));
    }

    public static <K, V> Map<K, V> parseMap(InputStream inputStream, Class<K> keyType, Class<V> valueType) {
        return parse(inputStream, mapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType));
    }

    public static <T> T convert(Object source, Class<T> type) throws IllegalArgumentException {
        return mapper.convertValue(source, type);
    }

    public static <T> T convert(Object source, JavaType type) throws IllegalArgumentException {
        return mapper.convertValue(source, type);
    }

    public static <T> T convert(Object source, TypeReference<T> ref) throws IllegalArgumentException {
        return mapper.convertValue(source, ref);
    }

    public static JavaType toJavaType(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            JavaType[] javaTypes = new JavaType[actualTypeArguments.length];
            for (int i = 0; i < javaTypes.length; i++) {
                javaTypes[i] = toJavaType(actualTypeArguments[i]);
            }
            return toJavaType(rawType, javaTypes);
        } else {
            return toJavaType((Class<?>) type);
        }
    }

    public static JavaType toJavaType(Class<?> rawType) {
        return SimpleType.constructUnsafe(rawType);
    }

    public static JavaType toJavaType(Class<?> rawType, JavaType parameterType) {
        return TypeFactory.defaultInstance().constructParametricType(rawType, parameterType);
    }

    public static JavaType toJavaType(Class<?> rawType, JavaType... parameterTypes) {
        return TypeFactory.defaultInstance().constructParametricType(rawType, parameterTypes);
    }
}
