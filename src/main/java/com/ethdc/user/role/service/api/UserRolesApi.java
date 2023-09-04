package com.ethdc.user.role.service.api;

import com.ethdc.user.role.service.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * The type User roles api.
 */
@RestController
@RequestMapping("/user/role")
@Slf4j
public class UserRolesApi {

    private static final Map<Integer, UserRole> USER_ROLE_MAP = new ConcurrentHashMap<>(1);

    /**
     * Instantiates a new User roles api.
     */
    public UserRolesApi() {
        IntStream.range(1, 100)
                .forEach(mapUserRole());

    }

    private static IntConsumer mapUserRole() {
        return id -> {
            USER_ROLE_MAP.put(id,
                    new UserRole(id, id, "some-role"));
        };
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    @GetMapping("/")
    public Flux<UserRole> getAll() {
        return Flux.fromIterable(USER_ROLE_MAP.values());
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    @GetMapping("/{id}")
    public Mono<UserRole> getById(@PathVariable Integer id) {
        return Mono.just(USER_ROLE_MAP.get(id));
    }

    /**
     * Gets by user id.
     *
     * @param id the id
     * @return the by user id
     */
    @GetMapping("/user/{id}")
    public Mono<UserRole> getByUserId(@PathVariable Integer id) {
        log.info("UserRolesApi::getByUserId({})", id);
        return Mono.just(USER_ROLE_MAP.values()
                        .stream()
                        .filter(userRole -> userRole.userId().equals(id))
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

}
