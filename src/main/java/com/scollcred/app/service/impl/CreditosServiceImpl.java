package com.scollcred.app.service.impl;

import com.scollcred.app.domain.Creditos;
import com.scollcred.app.repository.CreditosRepository;
import com.scollcred.app.service.CreditosService;
import com.scollcred.app.service.dto.FilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CreditosServiceImpl implements CreditosService {
    private final Logger log = LoggerFactory.getLogger(CreditosServiceImpl.class);
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CreditosRepository creditosRepository;

    public List<Creditos> allCreditos(FilterDTO filter){
        if(!(filter.getCliente() == null && filter.getMutual() == null) ){
            return findAll(filter);
        }
        return findAll();
    }

    public int creditosLength(FilterDTO filterDTO){
        return findAndGetLength(filterDTO);
    }

    private List<Creditos> findAll(){
        return findAll(new FilterDTO());
    }
    private List<Creditos> findAll(FilterDTO filter){
        filter.setPage(filter.getPage()*filter.getLimit());
        return creditosRepository.findAll(filter.getCliente(), filter.getMutual(), filter.getPage(),filter.getLimit());
    }

    private int findAndGetLength(FilterDTO filter){
      return creditosRepository.findAndGetLength(filter.getCliente(), filter.getMutual());
    }
}
