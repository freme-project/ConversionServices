package eu.freme.common.persistence.dao;

import eu.freme.common.persistence.model.Token;
import eu.freme.common.persistence.repository.TokenRepository;
import org.springframework.stereotype.Component;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@Component
public class TokenDAO extends DAO<TokenRepository, Token> {
}
