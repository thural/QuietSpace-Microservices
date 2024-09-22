package com.jellybrains.quietspace.user_service.repository.impl;

import com.jellybrains.quietspace.common_service.utils.PageUtils;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Profile> findById(String profileId) {
        try (Session session = sessionFactory.openSession()) {
            session.setDefaultReadOnly(true);
            Transaction tx = session.beginTransaction();
            try {
                String hql = "FROM Profile p WHERE p.id = :profileId";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setParameter("profileId", profileId);
                Optional<Profile> result = query.uniqueResultOptional();
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to get profile by id: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Optional<Profile> findByUserId(String userId) {
        try (Session session = sessionFactory.openSession()) {
            session.setDefaultReadOnly(true);
            Transaction tx = session.beginTransaction();
            try {
                String hql = "FROM Profile p WHERE p.userId = :userId";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setParameter("userId", userId);
                Optional<Profile> result = query.uniqueResultOptional();
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to get profile by userId: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Page<Profile> findAllByUsernameIsLikeIgnoreCase(String userName, Pageable pageable) {
        try (Session session = sessionFactory.openSession()) {
            session.setDefaultReadOnly(true);
            Transaction tx = session.beginTransaction();
            try {
                String hql = "FROM Profile p WHERE lower(p.username) LIKE lower(:username)";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setParameter("username", "%" + userName + "%");
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
                List<Profile> result = query.getResultList();
                tx.commit();
                return PageUtils.convertListToPage(result, pageable);
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to user profiles by name: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            session.setDefaultReadOnly(true);
            Transaction tx = session.beginTransaction();
            try {
                String hql = "FROM Profile p WHERE p.email = :email";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setParameter("email", email);
                Optional<Profile> result = query.uniqueResultOptional();
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to find user profile by id: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Page<Profile> findAllByQuery(String typedQuery, Pageable pageable) {
        try (Session session = sessionFactory.openSession()) {
            session.setDefaultReadOnly(true);
            Transaction tx = session.beginTransaction();
            try {
                String hql = "FROM Profile p WHERE p.username LIKE :query OR p.email LIKE :query";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setParameter("query", "%" + typedQuery + "%");
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
                List<Profile> result = query.getResultList();
                tx.commit();
                return PageUtils.convertListToPage(result, pageable);
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to find user profiles by query: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Optional<Profile> findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            session.setDefaultReadOnly(true);
            Transaction tx = session.beginTransaction();
            try {
                String hql = "FROM Profile p WHERE p.username = :username";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setParameter("username", username);
                Optional<Profile> result = query.uniqueResultOptional();
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to find user profile by username: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Page<Profile> findAllByUserIdIn(List<String> userIds, Pageable pageable) {
        try (Session session = sessionFactory.openSession()) {
            session.setDefaultReadOnly(true);
            Transaction tx = session.beginTransaction();
            try {
                String hql = "FROM Profile p WHERE p.userId IN (:userIds)";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setParameterList("userIds", userIds);
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
                List<Profile> result = query.getResultList();
                tx.commit();
                return PageUtils.convertListToPage(result, pageable);
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to get user profiles by id list: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                String hql = "DELETE FROM Profile p WHERE p.userId = :userId";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setParameter("userId", userId);
                query.executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to delete user profile by userId: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Page<Profile> findAll(Pageable pageable) {
        try (Session session = sessionFactory.openSession()) {
            session.setDefaultReadOnly(true);
            Transaction tx = session.beginTransaction();
            try {
                String hql = "FROM Profile";
                Query<Profile> query = session.createQuery(hql, Profile.class);
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
                List<Profile> result = query.getResultList();
                tx.commit();
                return PageUtils.convertListToPage(result, pageable);
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to get paged profiles: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public void deleteById(String profileId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Profile profile = session.get(Profile.class, profileId);
                if (profile != null) session.remove(profile);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to delete user profile by id: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Profile save(Profile profile) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(profile);
                tx.commit();
                return profile;
            } catch (Exception e) {
                tx.rollback();
                log.info("failed to save user profile: {}", e.getMessage());
                throw e;
            }
        }
    }
}
