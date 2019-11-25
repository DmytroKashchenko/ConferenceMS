package ua.dmytrokashchenko.conferencesms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

/*@Data
@NoArgsConstructor
@Entity*/
public class VisitingEntity {

//    private UserEntity user;
//    private PresentationEntity presentation;
//
//    private boolean visited;
// чому це не помістити в презентацію ???
    private PresentationEntity presentation; // presentation id
//    @ManyToMany
//    @JoinTable(name = "users_to_ratings")
    private Map<UserEntity, Boolean> isVisited;
}
