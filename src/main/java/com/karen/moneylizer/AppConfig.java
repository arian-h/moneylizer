package com.karen.moneylizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/public/")
				.resourceChain(false).addResolver(new CustomResourceResolver());
	}

	private class CustomResourceResolver implements ResourceResolver {
		private Resource index = new ClassPathResource("/public/index.html");
		private List<String> handledExtensions = Arrays.asList("css", "png",
				"svg", "jpg", "jpeg", "gif", "ico");

		private List<String> ignoredPaths = Arrays.asList("^api\\/.*$");

		@Override
		public Resource resolveResource(HttpServletRequest request,
				String requestPath, List<? extends Resource> locations,
				ResourceResolverChain chain) {
			return resolve(requestPath, locations);
		}

		@Override
		public String resolveUrlPath(String resourcePath,
				List<? extends Resource> locations, ResourceResolverChain chain) {
			Resource resolvedResource = resolve(resourcePath, locations);
			if (resolvedResource == null) {
				return null;
			}
			try {
				return resolvedResource.getURL().toString();
			} catch (IOException e) {
				return resolvedResource.getFilename();
			}
		}

		private Resource resolve(String requestPath,
				List<? extends Resource> locations) {
			if (isIgnored(requestPath)) {
				return null;
			}
			if (isHandled(requestPath)) {
				return locations
						.stream()
						.map(loc -> createRelative(loc, requestPath))
						.filter(resource -> resource != null
								&& resource.exists()).findFirst()
						.orElseGet(null);
			}
			return index;
		}

		private Resource createRelative(Resource resource, String relativePath) {
			try {
				return resource.createRelative(relativePath);
			} catch (IOException e) {
				return null;
			}
		}

		private boolean isIgnored(String path) {
			return !ignoredPaths.stream().noneMatch(
					rgx -> Pattern.matches(rgx, path));
		}

		private boolean isHandled(String path) {
			String extension = StringUtils.getFilenameExtension(path);
			return handledExtensions.stream().anyMatch(
					ext -> ext.equals(extension));
		}
	}

	// TODO remove this after active development of the front-end
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.exposedHeaders("Authorization", "Content-Type")
				.allowedMethods("*");
	}

}