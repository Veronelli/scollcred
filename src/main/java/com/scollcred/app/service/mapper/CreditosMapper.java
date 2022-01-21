package com.scollcred.app.service.mapper;

import com.scollcred.app.domain.Creditos;
import com.scollcred.app.service.dto.CreditosDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditosMapper{
    @Named("creditosDTOToCreditos")
    public static List<Creditos> creditosDTOToCreditos(CreditosDTO creditosdto){
        return creditosdto.getCredtios();
    }

    @Named("creditosToCreditosDTO")
    public static CreditosDTO creditosToCreditosDTO(List<Creditos> creditos, int length){
        return new CreditosDTO(creditos,length);
    }
}
