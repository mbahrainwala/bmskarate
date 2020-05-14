import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import { Player} from 'video-react';
import "./video-react.css";
import axios from 'axios';


import {
  FormGroup,
} from "@material-ui/core";

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

    const onChangeHandler=event=>{
                var data = new FormData()
               data.append('file', event.target.files[0])
               data.append('type', 'V')
               data.append('belt', '0')
              console.log(data);
            axios.post(`${props.globalData.serverURI}/api/uploadTrainingVideo`, data,
                         {headers: {
                               'Content-Type': 'multipart/form-data'
                         }}
                     )
                     .then(response => console.log(response.status))
                     .catch(err => console.warn(err));
          }
    //remove file playing logic from here
    const videoURI = `http://localhost:8080/api/download?token=${props.globalData.token}`;

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
                <form>
                    <FormGroup controlId='uploadFormId'>
                        <input type="file" name="file" onChange={onChangeHandler}/>
                    </FormGroup>
                </form>
            ):(null)}
        </CardActions>
    </Card>
    );
    }

export default ActionCard;