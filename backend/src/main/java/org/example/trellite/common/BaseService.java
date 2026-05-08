package org.example.trellite.common;

import java.util.List;

public interface BaseService<Req, Res> {

    List<Res> getAll();

    Res getById(Long id);

    Res save(Req dto);

    Res update(Long id, Req dto);

    void delete(Long id);

}
