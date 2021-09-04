package com.rony.services;

import com.rony.config.HibernateConfig;
import com.rony.models.Country;
import com.rony.models.Player;
import com.rony.models.Team;
import com.rony.models.User;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    @Autowired
    private HibernateConfig hibernateConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private CountryService countryService;

    public TeamService(HibernateConfig hibernateConfig, UserService userService, PlayerService playerService, CountryService countryService) {
        this.hibernateConfig = hibernateConfig;
        this.userService = userService;
        this.playerService = playerService;
        this.countryService = countryService;
    }

    public List<Team> allTeams(){
        var criteriaBuilder = hibernateConfig.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(Team.class);
        var root = criteriaQuery.from(Team.class);
        criteriaQuery.select(root);

        return hibernateConfig.getSession()
                .getEntityManagerFactory()
                .createEntityManager()
                .createQuery(criteriaQuery)
                .getResultList();
    }

  /*  public void saveCountry(Team teamDto, User userDto){
        System.out.println("save method of country service------------------------------------------------------");
        var session = hibernateConfig.getSession();
        Transaction tx = session.getTransaction();
        if(!tx.isActive()){
            tx = session.beginTransaction();
        }
        var countryEntity = new Country();
        BeanUtils.copyProperties(countryDto,countryEntity);
        countryEntity.setManagingDirector(userDto);
        session.save(countryEntity);
        session.flush();
        tx.commit();
        System.out.println("---------------------------------------------------");
        System.out.println("country is saved");
        System.out.println(countryEntity);
    } */

    public void deleteCountry(Country country){

    }

    public Team getTeamById(long id) {
        var criteriaBuilder = hibernateConfig.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(Team.class);
        var root = criteriaQuery.from(Team.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"),id));

        return hibernateConfig.getSession()
                .getEntityManagerFactory()
                .createEntityManager()
                .createQuery(criteriaQuery)
                .getSingleResult();
    }

    public void saveTeam(Team teamDto, long countryId, long[] playerIds, long coachId) {
        System.err.println("save method of Team service------------------------------------------------------");
        var session = hibernateConfig.getSession();
        Transaction tx = session.getTransaction();
        if(!tx.isActive()){
            tx = session.beginTransaction();
        }
        var teamEntity = new Team();
        BeanUtils.copyProperties(teamDto,teamEntity);
        var countryDto = countryService.getCountryById(countryId);
        teamEntity.setCountry(countryDto);
        var coachDto = userService.getUserById(coachId);
        teamEntity.setCoach(coachDto);
        List<Player> playerList = new ArrayList<>();
        for(long id: playerIds){
            playerList.add(playerService.getPlayerById(id));
        }
        teamEntity.setPlayerList(playerList);
        session.save(teamEntity);
        session.flush();
        tx.commit();
        System.err.println("---------------------------------------------------");
        System.err.println("team is saved");
        System.err.println(teamEntity);
    }

  /*
    public void updateCountry(Country countryDto, long id, long idMD, long[] playerIds) {
        System.err.println("update method of country service--------------------------------------------");
        var session = hibernateConfig.getSession();
        Transaction tx = session.getTransaction();
        if(!tx.isActive()){
            tx = session.beginTransaction();
        }
        var countryEntity = getCountryById(id);
        BeanUtils.copyProperties(countryDto,countryEntity);
        var managingDirectorDto = userService.getUserById(idMD);
        countryEntity.setManagingDirector(managingDirectorDto);
        List<User> playerList = new ArrayList<>();
        for(long pId: playerIds){
            playerList.add(userService.getUserById(pId));
        }
        countryEntity.setPlayerList(playerList);

        session.update(countryEntity);
        session.flush();
        tx.commit();
        System.err.println("---------------------------------------------------");
        System.err.println("country is updated");
        System.err.println(countryEntity);
    }

    */

}