// collection to store games
// as we decided to keep it simple - use only one record to store current game
Game = new Mongo.Collection("game");

const ROCK = "Rock";
const PAPER = "Paper";
const SCISSORS = "Scissors";
const SELECTED_RPS = "selectedRPS"; // value selected by player (rock, paper or scissors)
const PLAYER_1 = "player1";
const PLAYER_2 = "player2";
const PLAYER = "player";

if (Meteor.isClient) {

    Template.RPS.helpers({
        rpsParts: function () {
            return [{name:ROCK}, {name:PAPER}, {name:SCISSORS}]; // available variants
        },
        selectedRPS: function () {
            var p = Session.get(SELECTED_RPS); // value selected by player
            return p;
        },
        gameResult: function () {   // return result of the game if there is a result
            var game = Game.find().fetch()[0];
            var g1 = game[PLAYER_1]; // value selected by player 1
            var g2 = game[PLAYER_2]; // value selected by player 2
            if (g1 && g2) {
                var res = getGameResult(g1,g2); // return the winner
                return g1 + " : " + g2 + " , " + res; // just the text, keep it simple
            }
            return null; // there is no result yet
        }
    });

    Template.rpsPart.events({
        'click': function () { // select the value
            Session.set(SELECTED_RPS, this.name);
            var p = Session.get(PLAYER) === "player 1" ?
                PLAYER_1 : PLAYER_2;  // who made a choice
            var game = Game.find().fetch()[0]; // get current game
            game[p] = this.name; // update game (set user choice)
            Game.update(game._id,game);
        }
    });

    // return game results
    function getGameResult(g1,g2) {
        if (g1 === g2) return "Try Again";
        else if (g1 === ROCK && g2 === PAPER ||
            g1 === PAPER && g2 === SCISSORS ||
            g1 === SCISSORS && g2 === ROCK) return "Player 2 won";
        else
            return "Player 1 won";
    }

    // function to set current player and set a choice if this player made it already
    function pHelper(p) {
        Session.set(PLAYER,"player "+p); // set current player
        var game = Game.find().fetch()[0];
        if (!game) return p;
        var s = game[PLAYER+p];
        Session.set(SELECTED_RPS, s);
        return p;
    }

    Template.player1.helpers({
       player: function () {
           pHelper(1);
       }
    });

    Template.player2.helpers({
        player: function () {
            pHelper(2);
        }
    });

    // Continue button. Restart the game
    Template.result.events({
        'click': function () {
            var game = Game.find().fetch()[0];
            game[PLAYER_1] = null; // clear the results
            game[PLAYER_2] = null;
            Game.update(game._id,game);
        }
    });
}


if (Meteor.isServer) {
    Meteor.startup(function () {
        if (Game.find().count() === 0) {
                Game.insert({
                    player1: null,
                    player2: null
                });
        }
    });
}
