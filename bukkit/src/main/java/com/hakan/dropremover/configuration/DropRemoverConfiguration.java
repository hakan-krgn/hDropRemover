package com.hakan.dropremover.configuration;

import com.hakan.core.utils.yaml.HYaml;
import com.hakan.dropremover.DropRemoverPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"unchecked"})
public class DropRemoverConfiguration {

    public static DropRemoverConfiguration CONFIG;
    private final static Map<String, DropRemoverConfiguration> configurations = new HashMap<>();

    public static void initialize(DropRemoverPlugin plugin) {
        Arrays.asList(
                "config.yml",
                "items.yml"
        ).forEach(path -> configurations.put(path, new DropRemoverConfiguration(HYaml.create(plugin, path, path))));

        CONFIG = configurations.get("config.yml");
    }

    public static Map<String, DropRemoverConfiguration> getConfigurations() {
        return configurations;
    }

    public static Optional<DropRemoverConfiguration> findByPath(String path) {
        return Optional.ofNullable(configurations.get(path));
    }

    public static DropRemoverConfiguration getByPath(String path) {
        return DropRemoverConfiguration.findByPath(path).orElseThrow(() -> new NullPointerException("drop remover configuration couldn't for path: " + path));
    }


    private final HYaml yaml;

    public DropRemoverConfiguration(HYaml yaml) {
        this.yaml = yaml;
    }

    public HYaml getYaml() {
        return this.yaml;
    }

    public void save() {
        this.yaml.save();
    }

    public void reload() {
        this.yaml.reload();
    }

    public void delete() {
        this.yaml.delete();
    }

    public <T> Optional<T> find(String path) {
        return Optional.ofNullable((T) this.yaml.get(path));
    }

    public <T> Optional<T> find(String path, T tDefault) {
        Object object = this.yaml.get(path);
        return Optional.ofNullable((object != null) ? (T) object : tDefault);
    }

    public <T> Optional<T> find(String path, Class<T> clazz) {
        return Optional.ofNullable(clazz.cast(this.yaml.get(path)));
    }

    public <T> Optional<T> find(String path, T tDefault, Class<T> clazz) {
        Object object = this.yaml.get(path);
        return Optional.ofNullable((object != null) ? clazz.cast(this.yaml.get(path)) : tDefault);
    }

    public <T> T get(String path) {
        return (T) this.yaml.get(path);
    }

    public <T> T get(String path, T tDefault) {
        Object object = this.yaml.get(path);
        return (object != null) ? (T) object : tDefault;
    }

    public <T> T get(String path, Class<T> clazz) {
        return clazz.cast(this.yaml.get(path));
    }

    public <T> T get(String path, T tDefault, Class<T> clazz) {
        Object object = this.yaml.get(path);
        return (object != null) ? clazz.cast(this.yaml.get(path)) : tDefault;
    }

    public void set(String path, Object value) {
        this.yaml.set(path, value);
    }
}