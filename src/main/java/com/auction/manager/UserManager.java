package com.auction.manager;

import com.auction.exception.AuthenticationException;
import com.auction.exception.UserAlreadyExistsException;
import com.auction.model.Admin;
import com.auction.model.Bidder;
import com.auction.model.Seller;
import com.auction.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public final class UserManager {
    private static final UserManager INSTANCE = new UserManager();
    private final ConcurrentHashMap<String, User> usersById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, User> usersByName = new ConcurrentHashMap<>();
    private UserManager() {
    }

    /** @return thể hiện duy nhất của UserManager */
    public static UserManager getInstance() {
        return INSTANCE;
    }

    public User register(String username, String password, String role)
            throws UserAlreadyExistsException {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập và mật khẩu không được rỗng");
        }
        if (role == null) {
            throw new IllegalArgumentException("Thiếu vai trò");
        }

        String id = UUID.randomUUID().toString();
        User user;
        switch (role.toUpperCase()) {
            case User.ROLE_BIDDER:
                user = new Bidder(id, username, password);
                break;
            case User.ROLE_SELLER:
                user = new Seller(id, username, password);
                break;
            case User.ROLE_ADMIN:
                user = new Admin(id, username, password);
                break;
            default:
                throw new IllegalArgumentException("Vai trò không hợp lệ: " + role);
        }

        User existing = usersByName.putIfAbsent(username, user);
        if (existing != null) {
            throw new UserAlreadyExistsException("Tên đăng nhập đã tồn tại: " + username);
        }
        usersById.put(id, user);
        return user;
    }

    public User login(String username, String password) throws AuthenticationException {
        User user = (username == null) ? null : usersByName.get(username);
        // Không nói rõ sai tên hay sai mật khẩu, tránh lộ thông tin tài khoản nào tồn tại
        if (user == null || !user.checkPassword(password)) {
            throw new AuthenticationException("Sai tên đăng nhập hoặc mật khẩu");
        }
        return user;
    }

    /** @return người dùng theo id, hoặc null nếu không có */
    public User getUserById(String id) {
        return usersById.get(id);
    }

    /** @return danh sách tất cả người dùng (bản sao) */
    public List<User> getAllUsers() {
        return new ArrayList<>(usersById.values());
    }

    /**
     * Xóa người dùng. Chỉ Admin được phép.
     *
     * @param targetId  id người cần xóa
     * @param requester người yêu cầu xóa
     * @throws AuthenticationException nếu người yêu cầu không phải Admin
     */
    public void removeUser(String targetId, User requester) throws AuthenticationException {
        if (!(requester instanceof Admin)) {
            throw new AuthenticationException("Chỉ Admin được xóa người dùng");
        }
        User removed = usersById.remove(targetId);
        if (removed != null) {
            usersByName.remove(removed.getName());
        }
    }
}
