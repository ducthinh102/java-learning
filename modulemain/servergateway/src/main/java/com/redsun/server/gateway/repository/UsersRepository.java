
package com.redsun.server.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redsun.server.gateway.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {

	@Query(value="UPDATE Users users SET users.password = :password WHERE users.username = :username")
	Integer updatePassword(@Param("username") String username, @Param("password") String password);
	
	Users findByUsername(String username);

}