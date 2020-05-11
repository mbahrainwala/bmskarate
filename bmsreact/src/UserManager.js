import React from "react";
import TypoGraphy from '@material-ui/core/Typography'
import {
  Button
} from "@material-ui/core";

const UserManager = props => {
    return(
        <>
        {props.globalData.loginUser!=null?(
            <>
             <Button size="small">
                &nbsp;&nbsp;{props.globalData.loginUser.firstName}
                &nbsp;&nbsp;{props.globalData.loginUser.lastName}
             </Button>
             <Button size="small">Logout</Button>
            </>
         ):(<></>)}
         </>
    );
}

export default UserManager;