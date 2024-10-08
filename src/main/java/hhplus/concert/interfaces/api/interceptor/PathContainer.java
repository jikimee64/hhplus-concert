package hhplus.concert.interfaces.api.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class PathContainer {

    private final PathMatcher pathMatcher;
    private final List<RequestPath> includePathPattern;
    private final List<RequestPath> excludePathPattern;

    public PathContainer() {
        this.pathMatcher = new AntPathMatcher();
        this.includePathPattern = new ArrayList<>();
        this.excludePathPattern = new ArrayList<>();
    }

    public boolean notIncludedPath(String targetPath, String pathMethod) {
        boolean excludePattern = excludePathPattern.stream()
                .anyMatch(requestPath -> anyMatchPathPattern(targetPath, pathMethod, requestPath));

        boolean includePattern = includePathPattern.stream()
                .anyMatch(requestPath -> anyMatchPathPattern(targetPath, pathMethod, requestPath));

        return excludePattern || !includePattern;
    }

    private boolean anyMatchPathPattern(String targetPath, String pathMethod, RequestPath requestPath) {
        return pathMatcher.match(requestPath.getPathPattern(), targetPath) &&
                requestPath.matchesMethod(pathMethod);
    }

    public void includePathPattern(String targetPath, PathMethod pathMethod) {
        this.includePathPattern.add(new RequestPath(targetPath, pathMethod));
    }

    public void excludePathPattern(String targetPath, PathMethod pathMethod) {
        this.excludePathPattern.add(new RequestPath(targetPath, pathMethod));
    }
}