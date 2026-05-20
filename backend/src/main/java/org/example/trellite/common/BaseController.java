package org.example.trellite.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

public interface BaseController<Req, Res, ID> {

    ResponseEntity<List<Res>> getAll();

    ResponseEntity<Res> getById(@PathVariable ID id);

    ResponseEntity<Res> create(@RequestBody Req req);

    ResponseEntity<Res> update(@PathVariable ID id, @RequestBody Req req);

    ResponseEntity<Void> delete(@RequestBody ID id);

}
