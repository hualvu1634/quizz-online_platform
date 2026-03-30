package huan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import huan.backend.entity.Progress;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

}