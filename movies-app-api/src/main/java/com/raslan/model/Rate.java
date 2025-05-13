package com.raslan.model;

import jakarta.persistence.*;

@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user ;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Movie movie ;


}
