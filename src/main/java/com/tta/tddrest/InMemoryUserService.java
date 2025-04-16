package com.tta.tddrest;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class InMemoryUserService {

    private final Map<Long, User> map = new HashMap<>();
    private final SecureRandom random = new SecureRandom();

    static final Long EXISTING_USER_ID = 1712877605463L;

    {
        User existing = User.builder()
                .userId(EXISTING_USER_ID)
                .username("captain")
                .firstName("Steve")
                .lastName("Rogers")
                .build();
        map.put(EXISTING_USER_ID, existing);
    }

    User findById(long id) {
        return map.get(id);
    }

    User create(User user) {
        validateUser(user);

        // create a new user from the passed one
        User newUser = user.toBuilder().userId(random.nextLong()).build();
        map.put(newUser.getUserId(), newUser);
        return map.get(newUser.getUserId());
    }

    private void validateUser(User user) {
        ofNullable(user).filter(u -> {
            // Should not have userId set if creating a new one
            // Rest, just check for non-null fields
            return ofNullable(u.getUserId()).isEmpty()
                    && ofNullable(u.getUsername()).isPresent()
                    && ofNullable(u.getFirstName()).isPresent()
                    && ofNullable(u.getLastName()).isPresent();
        }).orElseThrow(() -> new IllegalArgumentException("User to create is invalid: " + user));
    }

}
