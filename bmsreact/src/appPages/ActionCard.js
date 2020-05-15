import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';

const useStyles = makeStyles((theme) => ({
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

    paper: {
        display: 'flex',
        flexWrap: 'wrap',
        '& > *': {
        margin: theme.spacing(0),
        width: theme.spacing(20),
        height: theme.spacing(6),
        },
    },
}));

const ActionCard = props => {
    const classes = useStyles();

    const openBelt = (beltid) =>{
        console.log(beltid);
    };

    return (
        <Card className={classes.root}>
            <CardContent>
                <Typography variant="h5" component="h2">
                {props.desc}
                </Typography>
                {props.type==='u'?(
                    <div className={classes.paper}>
                        <Button color="primary" size="medium" key='users' onClick={()=>{
                                const newProps = {...props.globalData};
                                                    newProps.appPage='listUser';
                                                    props.setGlobalData(newProps);
                            }}>Open</Button>
                    </div>
                    ):null
                }
                {props.type==='s'?(<>
                    {props.globalData.loginUser.type !== 'U'?(
                        <div className={classes.paper}>
                            <Button color="primary" key='addStudent' onClick={()=>{}}>
                                Add Student</Button>
                            <Button color="primary" key='listStudent' onClick={()=>{}}>
                                Show Students</Button>
                        </div>
                    ):null}
                </>):null}
                {props.type==='v'?(
                    <div className={classes.paper}>
                        {props.globalData.commonData.belts.map(belt=>(
                            <Button key={belt.beltId} beltid={belt.beltId} onClick={()=>{openBelt(belt.beltId)}}>
                                {belt.beltColor}</Button>
                            )
                        )}
                    </div>
                ):null}
            </CardContent>
        </Card>
    );
    }

export default ActionCard;