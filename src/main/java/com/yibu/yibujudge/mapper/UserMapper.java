package com.yibu.yibujudge.mapper;

import com.yibu.yibujudge.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    int insert(User user);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User getUserByEmail(@Param("email") String email);

    @Select("select * from user where username = #{username} and password = #{password}")
    User loginByUsername(@Param("username") String username, @Param("password") String password);

    @Select("select * from user where id = #{id} ;")
    User getUserById(@Param("id") Long id);

    int update(User user);

    @Select("select * from user where username = #{username}")
    User getUserByUserNam(@Param("username") String username);

    List<User> getUserByIds(List<Long> ids);

    @Update("update user set submit_count = submit_count + 1, ac_count = ac_count + #{isAccepted} where id = #{userId}")
    void updateSubmitCount(@Param("userId") Long userId, @Param("isAccepted") int isAccepted);
}
