package org.example.trellite.common;

import java.util.List;

@Deprecated
public interface BaseService<Req, Res, ID> {

    List<Res> getAll();

    Res getById(ID id);

    Res save(Req dto);

    Res update(ID id, Req dto);

    void delete(ID id);

}
