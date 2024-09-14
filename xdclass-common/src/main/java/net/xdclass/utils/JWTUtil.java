package net.xdclass.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.LoginUser;

import java.util.Date;

/**
 * JWT工具类
 *
 * @author tangcj
 * @date 2024/09/14 09:36
 **/
@Slf4j
public class JWTUtil {

    /**
     * token 过期时间，正常是7天，方便测试我们改为70
     */
    private static final long EXPIRE = 1000 * 60 * 60 * 24 * 7 * 10;

    /**
     * 加密的秘钥
     */
    private static final String SECRET = "xdclass.net666";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "xdclass1024shop";

    /**
     * subject 颁布者
     */
    private static final String SUBJECT = "xdclass";

    /**
     * 根据用户信息，生成令牌
     *
     * @param user
     * @return
     */
    public static String geneJsonWebToken(LoginUser user) {
        Long userId = user.getId();
        String token = Jwts.builder().setSubject(SUBJECT)
                //payload
                .claim("head_img", user.getHeadImg())
                .claim("id", userId)
                .claim("name", user.getName())
                .claim("mail", user.getMail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                //签名算法、密钥
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
        token = TOKEN_PREFIX + token;
        return token;
    }

    /**
     * 校验token的方法
     *
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
            return claims;
        } catch (Exception e) {
            log.info("jwt token解密失败");
            return null;
        }
    }
}
