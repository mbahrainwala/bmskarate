import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import { Player, ControlBar  } from 'video-react';
import "./video-react.css";

const useStyles = makeStyles({
  root: {
    minWidth: 275,
  },
  bullet: {
    display: 'inline-block',
    margin: '0 2px',
    transform: 'scale(0.8)',
  },
  title: {
    fontSize: 14,
  },
  pos: {
    marginBottom: 12,
  },
});

const ActionCard = props => {
const classes = useStyles();
  const bull = <span className={classes.bullet}>•</span>;
  const videoURI = `http://localhost:8080/api/download?token=${props.globalData.token}`;

  const launch = () =>{
    if(props.type === 'user'){}
  }

    return (
    <Card className={classes.root}>
        <CardContent>
            <Typography variant="h5" component="h2">
            {props.desc}
            </Typography>
        </CardContent>
        <CardContent>
            {props.type==='v'?(<div style={{width:250}}>
                <Player
                playsInline
                preload='none'
                fluid='false'
                src={videoURI}
                />
                </div>):(null)
            }
        </CardContent>
        <CardActions>
            {props.type==='v'?(
                <input type="file" name="file" id="file"/>
            ):(null)}
        </CardActions>
    </Card>
    );
    }

export default ActionCard;