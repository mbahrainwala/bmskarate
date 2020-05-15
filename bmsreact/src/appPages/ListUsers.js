import React, {useState} from 'react';

import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  Button
} from "@material-ui/core";

import {restCall} from '../utils/RestComponent'


const ListUsers = props => {
    const [apiRet, setApiRet] = useState({});

    const handleChange=(event)=>{
        if(event.target.id==='lastName'){
            restCall('GET', `${props.serverURI}/api/findUser?lastName=${event.target.value}`, setApiRet, props.token, null);
        }
    }

    return(
        <>
            <div
                style={{
                display: "flex",
                justifyContent: "center",
                margin: 20,
                padding: 20
                }}
            >
               <div style={{width:"80%"}}>
                    <form onChange={handleChange.bind(this)}>
                        <h1>List Users</h1>
                        {apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
                        <FormControl margin="normal" fullWidth>
                            <InputLabel htmlFor="lastName">Last Name</InputLabel>
                            <Input id="lastName" type="text"/>
                        </FormControl>
                    </form>
                </div>
            </div>

            <div
                style={{
                display: "flex",
                justifyContent: "center",
                margin: 20,
                padding: 20
                }}
            >
            <TableContainer component={Paper} style={{width:"80%"}}>
                <Table aria-label="simple table">
                <TableHead>
                  <TableRow>
                    <TableCell>First Name</TableCell>
                    <TableCell>Last Name</TableCell>
                    <TableCell>Email Id</TableCell>
                    <TableCell>Phone</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>&nbsp;</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                    {apiRet!== undefined
                        && apiRet!==null && apiRet.error === undefined
                        && Object.getOwnPropertyNames(apiRet).length !== 0?(
                        <>
                            {apiRet.map(user=>(
                                <TableRow key={user.id}>
                                    <TableCell component="th" scope="row">
                                        {user.firstName}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.lastName}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.emailId}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.phone}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.type==='U'?(<>User</>):(null)}
                                        {user.type==='A'?(<div style={{color:'blue'}}>Admin</div>):(null)}
                                        {user.type==='S'?(<div style={{color:'red'}}>Super User</div>):(null)}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.id!==props.globalData.loginUser.id?(
                                            <Button onClick={()=>{
                                                const newProps = {...props.globalData};
                                                newProps.appPage='editUser';
                                                newProps.editUser=user;
                                                props.setGlobalData(newProps);
                                                }}>Edit</Button>
                                            ):(null)}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </>
                    ):(null)}
                </TableBody>
                </Table>
            </TableContainer>
            </div>
            </>
    );
}

export default ListUsers;