package org.example.trellite.common;

import java.util.List;

public interface BaseService<Req, Res> {

    List<Req> getAll();

    Req getById(Long id);

    Req update(Long id, Res dto);

    void delete(Long id);

}
