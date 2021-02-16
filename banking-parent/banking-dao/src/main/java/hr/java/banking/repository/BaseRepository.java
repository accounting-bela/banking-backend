package hr.java.banking.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hr.java.banking.entities.BaseModel;

@Repository
public interface BaseRepository<T extends BaseModel> extends CrudRepository<T, UUID> {

}
