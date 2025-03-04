package com.sparta.user.application.service.user;

import com.sparta.commons.domain.exception.BusinessException;
import com.sparta.user.application.dto.UserInfo;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.model.vo.UserRole;
import com.sparta.user.domain.repository.user.UserRepository;
import com.sparta.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public UserInfo getUserInfo(Long userId) {
    return userRepository.findById(userId)
        .map(UserInfo::create)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
  }

  @Transactional
  public void updateUserAuthority(Long userId, UserRole role) {
    User user = this.validateUser(userId);
    user.updateAuthority(role);
  }

  public User validateUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
  }

  @Transactional
  public void deleteUser(Long userId) {
    User user = this.validateUser(userId);
    user.softDelete(user.getUsername());
  }

  @Transactional(readOnly = true)
  public Page<UserInfo> getUserInfos(Long userId, String keyword, Pageable pageable) {
    return userRepository.findUserInfos(userId, keyword, pageable);
  }
}
