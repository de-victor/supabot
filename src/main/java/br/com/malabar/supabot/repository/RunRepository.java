package br.com.malabar.supabot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.malabar.supabot.entity.Run;

@Repository
public interface RunRepository extends CrudRepository<Run, Long>, JpaSpecificationExecutor<Run> {
    
    @Query(value = "select * from runs r inner join games g on r.game_id = g.id inner join runners ru on r.runner_id = ru.id inner join categories c on c.id = r.category_id where ru.name = :runnerName order by r.place limit 3", nativeQuery = true)
    public List<Run> findRunByRunnerLimit3(String runnerName);

    @Transactional
    @Modifying
    @Query(value = "update runs r set r.default_run = true where id = :id", nativeQuery = true)
    public void updateDefaulByRunId(Long id);

    @Transactional
    @Modifying
    @Query(value = "update runs r set r.default_run = false", nativeQuery = true)
    public void disableDefaultRuns();
    
}
